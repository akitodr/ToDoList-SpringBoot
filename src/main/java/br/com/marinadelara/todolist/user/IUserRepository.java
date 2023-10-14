package br.com.marinadelara.todolist.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

//Passo entre os <> qual a classe da entidade, e qual o tipo de id que ela possui
public interface IUserRepository extends JpaRepository<UserModel, UUID> {
    UserModel findByUsername(String username);
}
