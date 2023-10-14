package br.com.marinadelara.todolist.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.marinadelara.todolist.user.IUserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {
    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //Só vamos executar a autenticação de usuário quando a rota for /tasks
        //Para qualquer outra rota, não queremos realizar autenticação
        var servletPath = request.getServletPath();
        //usar startsWith para que toda rota derivada de tasks funcione
        if(servletPath.startsWith("/tasks/")) {
            //Executar alguma ação
            //Se o usuário estiver cadastrado: a rota pode continuar
                //1. Pega a autenticação (user e password)
                //2. Valida user
                //3. Valida password
                //4. Se tudo OK, rota continua
            //Se o usuário não estiver cadastrado: interrompe a continuação da rota

            //Vai pegar o usuário e senha da requisição
            var auth = request.getHeader("Authorization");
            //removemos a palavra Basic (tipo de autenticação que estamos fazendo)
            //para ficar só com o código de retorno da requisição
            var authEncoded = auth.substring("Basic".length()).trim();
            //decodificamos o código da requisição para um array de bytes
            byte[] authDecoded = Base64.getDecoder().decode(authEncoded);
            //convertemos o array de bytes em um tipo string
            String authString = new String(authDecoded);
            //armazenamos na posição 0 o user (amarinadelara) e na posição 1 a senha (qwer1234)
            String[] credentials = authString.split(":");

            String username = credentials[0];
            String password = credentials[1];

            //Validar informações do usuário:
            var user = userRepository.findByUsername(username);
            if(user == null) {
                response.sendError(401);
            } else {
                // Validar senha:
                //1. Verifico se a senha que foi recebida na requisição é a mesma senha armazenada no usuário
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                //verified retorna true or false
                if(passwordVerify.verified) {
                    //request é o que vem da minha requisição e responde é o que eu passo para o usuário (json)
                    //para setar o atributo userId, vamos passar o Id para a TaskController
                    request.setAttribute("userId", user.getId());
                    //Tudo certo pode seguir caminho
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(401);
                }
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
