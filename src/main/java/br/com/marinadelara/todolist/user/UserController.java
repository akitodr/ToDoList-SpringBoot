package br.com.marinadelara.todolist.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@Controller //Utiliza quando quer criar estrutura com páginas (retorna páginas/templates) flexibilidade maior de retorno
@RestController //Utiliza para construir uma API REST (retorno de objeto/rotas)
@RequestMapping("/users") //Faz o mapeamento da classe "MyFirstController" para uma rota http
public class UserController {

    @Autowired
    private IUserRepository userRepository;

    @PostMapping("/") //Indica que o método myFirstMessage é do tipo post (inserção) e define a rota de chamada
    //ResponseEntity retorna em caso de sucesso ou erro, para tratar quando o cadastro deu certo e não deu certo
    //RequestBody Indica que os dados da requisição estão no corpo da mensagem e serão convertidos para o objeto
    //especificado no parâmetro do método
    public ResponseEntity create(@RequestBody UserModel userModel) {
        var user = this.userRepository.findByUsername(userModel.getUsername());

        if(user != null) {
            //Mensagem de erro
            //Status code (error XXX na API)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já cadastrado!");
        }

        //Biblioteca para criptografar senhas no banco
        var passwordHashed = BCrypt.withDefaults()
            .hashToString(12, userModel.getPassword().toCharArray());

        userModel.setPassword(passwordHashed);

        var userCreated = this.userRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }
}
