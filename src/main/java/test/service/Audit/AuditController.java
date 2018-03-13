package test.service.Audit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@SpringBootApplication
public class AuditController extends SpringBootServletInitializer{
	
	@Autowired
	private UserRepository userRepository;
	
	@Value("${topic}")
	private String topic;
	
	@RequestMapping("/auditlog")
	public @ResponseBody Iterable<AuditEntries> getAuditEntries()
	{
		return userRepository.findAll();
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) 
	{
	    return application.sources(AuditController.class);
	}
	
	@KafkaListener(topics = "test", group = "test-consumer-group")
	public void listen(String message)
	{
		if(message.contains(":"))
		{
			String[] arr = message.split(":");
			AuditEntries entry = new AuditEntries();
			entry.setReqType(arr[0]);
			entry.setDescription(arr[1]);
			userRepository.save(entry);
		}
	}

}
