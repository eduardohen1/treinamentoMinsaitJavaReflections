package br.com.ehmf.webframework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) //Anotação em tempo de execução
@Target(ElementType.TYPE) //qual target pode receber essa anotação aplicando à uma Classe
public @interface WebframeworkController {

}
