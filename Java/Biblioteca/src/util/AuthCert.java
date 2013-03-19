/*
 */
package util;

import java.security.cert.X509Certificate;
import java.util.StringTokenizer;

public final class AuthCert {

    private String name, lastname, dni,publicKey;

    public AuthCert(X509Certificate cert) {
        setPublicKey(cert.getPublicKey().toString());
        StringTokenizer st = new StringTokenizer(cert.getSubjectX500Principal().toString(), ",");
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (token.startsWith("GIVENNAME", 1)) {
                int auxIndex = token.lastIndexOf("=");
                String aux = token.substring(auxIndex + 1);
                setName(aux);
            }
            if (token.startsWith("SURNAME", 1)) {
                int auxIndex = token.lastIndexOf("=");
                String aux = token.substring(auxIndex + 1);
                setLastname(aux);
            }


            if (token.startsWith("SERIALNUMBER", 1)) {
                int auxIndex = token.lastIndexOf("=");
                String aux = token.substring(auxIndex + 1);
                setDni(aux);
            }
        }
    }

    public String getPublicKey() {
        return publicKey;
    }

    private void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    private void setName(String name) {
        this.name = name;
    }

    private void setLastname(String lastname) {
        this.lastname = lastname;
    }

    private void setDni(String dni) {
        this.dni = dni;
    }

    public String getName() {
        return capitalize(name);
    }

    public String getLastname() {
        return capitalize(lastname);
    }

    public String getDni() {
        return capitalize(dni);
    }

    private static String capitalize(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }
}
