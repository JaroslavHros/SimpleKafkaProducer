
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.PartitionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static List<PartitionInfo> partitionsFor;

    public static void main(String[] args) throws Exception {

        //* Properties */ 
        Properties prop = new Properties();
        String topic = "myFirstTopic";
        prop.setProperty("bootstrap.servers", "192.168.56.102:29092");
        prop.setProperty("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
        prop.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        prop.setProperty("acks", "all");  
        prop.setProperty("request.timeout", "100000");
        prop.setProperty("batchSize", "1");

        //Logger logger= LoggerFactory.getLogger(App.class);  
        KafkaProducer<Integer, String>  producer = new KafkaProducer<Integer, String>(prop);
        partitionsFor = producer.partitionsFor(topic);
// verify client connection before producing
        try {
                KafkaAdminClient client = new KafkaAdminClient("192.168.56.102:29092");
                boolean check = client.verifyConnection();
                System.err.println("client can communicate to the broker:" + check);
        } catch (Exception e) {
            System.err.println("client is unable communicate to the broker");
            e.printStackTrace();
        }
// check available partitions 
        try {
            for (int i = 0 ; i < partitionsFor.size() ; i ++) {
                System.out.println(partitionsFor.get(i));
                //logger.debug(partitionsFor.get(i).toString());   
            }
        } catch (Exception e) {
            
            e.printStackTrace();
        }

        //System.out.println("preparing messagess to push to the topic");

        // Creating and sending the messages:
       try{
        for(int i = 0; i < 500; i++){
            String value =  "Value pushed to the topic " + " in iteration "+ Integer.toString(i);
            Integer key = 0;
            ProducerRecord<Integer, String>  record = new ProducerRecord<Integer, String>(topic,  key + i, value);
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {  
    
            if (e== null) {  
                System.out.println("Successfully received the details as: \n" +  
                        "Topic:" + recordMetadata.topic() + "\n" +  
                        "Partition:" + recordMetadata.partition() + "\n" +  
                        "Offset" + recordMetadata.offset() + "\n" +  
                        "Timestamp" + recordMetadata.timestamp() + "\n" + 
                        "Valuesize" + recordMetadata.serializedValueSize());  
                          }  
             else {  
                 System.out.println("Can't produce,getting error"); 
                 e.printStackTrace();
            } 
        }
            }).get();
                 
 }
    }catch(Exception e){
        e.printStackTrace();;
    }
finally{
producer.close();
System.out.println("Successfully closed the client!");
}
}
}