package com.devsuperior.dsclients.services.utils;

import com.devsuperior.dsclients.services.exceptions.UtilsValidationException;

public class Format {
	
    private static final int SIZE_OF_CPF = 11;  
	
	public static String formatCpf(String cpf) throws UtilsValidationException {  
        if (cpf != null) {  
            cpf = cpf.replaceAll("\\D", "");      
            if (cpf.length() == SIZE_OF_CPF) {  
                return cpf.replaceAll("([0-9]{3})([0-9]{3})([0-9]{3})([0-9]{2})","$1\\.$2\\.$3-$4");  
            }  
        }  
        throw new UtilsValidationException("Cpf inválido.");  
    }  
}
