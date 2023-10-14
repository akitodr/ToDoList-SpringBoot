package br.com.marinadelara.todolist.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

public class Utils {
    //Metodo para mesclar informações nulas e não nulas entre dois objetos (source e target)
    public static void copyNonNullProperties(Object source, Object target) {
        //Método da classe do java que copia os atributos de uma classe para outra a partir de uma regra
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    //Metodo para pegar todas as propriedades nulas de uma requisição
    //Retorna um array de string e recebe um objeto qualquer
    public static String[] getNullPropertyNames(Object source) {
        //Interface do java que oferece uma forma de acesso às propriedades de um objeto no java
        //BeanWrapperImpl é a implementação da interface
        final BeanWrapper src = new BeanWrapperImpl(source);

        //Obter os atributos do objeto
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        //Criar um conjunto com os atributos nulos (null)
        Set<String> emptyNames = new HashSet<>();

        //Inserindo atributos nulos no conjunto emptyNames
        for(PropertyDescriptor pd: pds) {
            //Pega o valor do atributo pelo nome
            Object srcValue = src.getPropertyValue(pd.getName());
            //Se o valor for nulo
            if(srcValue == null) {
                //Adiciona no conjunto emptyNames
                emptyNames.add(pd.getName());
            }
        }

        //Converte o conjunto em um array de string
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
