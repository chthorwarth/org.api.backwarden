
import adapters.database.CredentialAdapter;
import logic.ports.input.CredentialAPI;
import logic.ports.output.persistence.CredentialRepository;
import logic.services.CredentialService;

public class Main
{
	public static void main(String[] args)
	{
//Test
		CredentialRepository repository = new CredentialAdapter();
		CredentialAPI api = new CredentialService(repository);
		//CredentialController controller = new CredentialController(api);
	}
}
