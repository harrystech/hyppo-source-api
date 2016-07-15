# Hyppo Source API

This API is used by hyppo data ingestion integrations. It has minimal dependencies itself and is the only
required depdency that integrations must have in order to function.

All other choices of library, JVM language (scala, clujure, kotlin, etc) are available to the integration
implementation.

## Data Ingestion
Each data ingestion source that you create an integration for will have several methods in common:

 * ***AvroRecordType&lt;T&gt; avroRecord:*** This is the Avro Record Type of the processed data for a given integration
 * ***ValidationResult validateSourceConfiguration(final IngestionSource source):*** This is the method a given integration will override to define what a valid Configuration object for your integration looks like. This is what an integratiom will check for the existence of things like an api-key, base-uri, or database connection string.
 * ***ValidationResult validateJobParameters(final DataIngestionJob job):*** This method is overriden to check for any job specific parameters. It's possible, even likely, a given job won't have any.
 * ***ValidationResult validateTaskArguments(final DataIngestionTask task):*** This method is overriden to check for any task specific arguments. Examples might include a start and/or end time, an endpoint, or anything else specific to the data ingestion source.
 * ***IngestionTaskCreator newIngestionTaskCreator():*** Essentially a factory for creating some number of DataIngestionTasks.
 * ***ProcessedDataPersister&lt;T&gt; newProcessedDataPersister():*** Returns an object that contains integration specific logic for persisting the processed Avro data into the data store of choice.
  
#### IngestionTaskCreator
In defining your IngestionTaskCreator object you'll need to add the following import line to your main integration class file: `import com.harrys.hyppo.source.api.task._`. Your factory class will need to implement a single method `void createIngestionTasks(CreateIngestionTasks operation)`. This method will create *n* tasks that will later be processed by the Hyppo worker, executing the integration specific code defined in your main integration class. Here is an example in Scala for an IngestionTaskCreator:

```scala

    new IngestionTaskCreator {
        override def createIngestionTasks(operation: CreateIngestionTasks): Unit = {
            var startTime        = defaultStartTime
            val endTime          = defaultEndTime
            val fourHours : Long = 14400
            
            while (startTime < endTime)  {
                val localEndTime = startTime + (fourHours -1)
                val chargeArgs = JavaConversions.mapAsJavaMap(Map[String, AnyRef]("endpoint" -> "charges", "startTime" -> startTime.toString, "endTime" -> localEndTime.toString))
                operation.createTaskWithArgs(chargeArgs)
                startTime += fourHours
            }
        }
    }
```

#### ProcessedDataPersister&lt;T&gt;
The ProcessedDataPersister of type T contains the application specific logic for taking processed data stored as AvroRecords and translating those into some sort of persistable record type to be stored. To be a valid ProcessedDataPersister you must override at least `void persistProcessedData(PersistProcessedData<T> operation)`. 

Optionally, you can override `default PersistingSemantics semantics()`. This method defauls to `PersistingSemantics.Unsafe`, but if implemented the persisting semantics for an integration can be set to `PersistingSemantics.Idempotent`.

This class will contain all application logic around how you store data into whichever data store you are using. At Harry's we use jOOQ to handle all of our JDBC connections to a Postgres instance and have been very please with the library. Since the operations here are most often inserts and updates, using a simple SQL DSL like jOOQ is sufficient.

## Creating A RawData Ingestion

## Creating A Processed Data Ingestion

## Complete Example In Scala
