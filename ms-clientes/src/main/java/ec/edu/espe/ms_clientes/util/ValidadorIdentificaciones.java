package ec.edu.espe.ms_clientes.util;

public class ValidadorIdentificaciones {
    public static boolean validarCedula(String cedula) { // Validar cedula ecuatoriana usando el algoritmo modulo 10
        if (cedula == null || cedula.length() != 10) { // Valida longitud y que no sea nulo
            return false;
        }

        if (!cedula.matches("\\d+")) { // Verifica que solo sean numeros
            return false;
        }

        int provincia = Integer.parseInt(cedula.substring(0, 2)); // Valida codigo de provincia, los dos primeros dígitos deben estar entre 1 y 24
        if (provincia < 1 || provincia > 24) {
            return false;
        }

        int tercerDigito = Integer.parseInt(cedula.substring(2, 3)); // Valida el tercer dígito que debe ser menor a 6 para personas naturales
        if (tercerDigito >= 6) {
            return false;
        }

        int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2}; // Uso del algoritmo modulo 10
        int total = 0;
        int digitoVerificador = Integer.parseInt(cedula.substring(9, 10));

        for (int i = 0; i < coeficientes.length; i++) {
            int valor = Integer.parseInt(String.valueOf(cedula.charAt(i))) * coeficientes[i];
            total += (valor >= 10) ? (valor - 9) : valor;
        }

        int decenaSuperior = (int) (Math.ceil(total / 10.0) * 10);
        int resultado = decenaSuperior - total;

        if (resultado == 10) {
            resultado = 0;
        }

        return resultado == digitoVerificador;
    }

    // Cuando es privado son 13 digitos y el tercer digito es 9
    public static boolean validarRucPersonaJuridica(String ruc) { // Validar RUC de persona juridica (en este caso privada) usando el algoritmo modulo 11
        if (ruc == null || ruc.length() != 13) { // Valida longitud y que no sea nulo
            return false;
        }

        if (!ruc.endsWith("001")) { // Verifica que termine en 001 que es la sucursal principal
            return false;
        }

        int provincia = Integer.parseInt(ruc.substring(0, 2)); //Valida código de provincia
        if (provincia < 1 || provincia > 24) {
            return false;
        }

        int tercerDigito = Integer.parseInt(ruc.substring(2, 3)); // Valida el tercer digito que debe ser 9 en este caso
        if (tercerDigito != 9) {
            return false;
        }

        int[] coeficientes = {4, 3, 2, 7, 6, 5, 4, 3, 2}; // Uso del algoritmo modulo 11
        int total = 0;
        int digitoVerificador = Integer.parseInt(ruc.substring(9, 10));

        for (int i = 0; i < coeficientes.length; i++) {
            int valor = Integer.parseInt(String.valueOf(ruc.charAt(i))) * coeficientes[i];
            total += valor;
        }

        int residuo = total % 11;
        int resultado;

        if (residuo == 0) {
            resultado = 0;
        } else {
            resultado = 11 - residuo;
        }

        return resultado == digitoVerificador;
    }
}