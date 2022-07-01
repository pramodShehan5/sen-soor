# sen-soor
Here using Akka-stream to process sensor stream easy.

Source -> Flow -> Sink

CSV files -> convert to ByteString -> lineDelimiter -> create case class for each sensor record -> send to actor and add it to the hashmap -> print result
