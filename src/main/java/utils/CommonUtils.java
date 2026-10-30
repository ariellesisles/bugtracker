package utils;


import tools.jackson.core.util.DefaultIndenter;
import tools.jackson.core.util.DefaultPrettyPrinter;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

public class CommonUtils {

    public static String prettyPrintToken(String accessToken) {

        try {
            String json = decodeJsonBody(accessToken);

            ObjectMapper mapper = new ObjectMapper();

            DefaultPrettyPrinter.Indenter indenter = new DefaultIndenter("  ", DefaultIndenter.SYS_LF);
            DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
            printer.indentObjectsWith(indenter);
            printer.indentArraysWith(indenter);

            Map tokenBodyMap = mapper.readValue(json, Map.class);

            return mapper.writer().with(printer).writeValueAsString(tokenBodyMap);
        } catch (Exception ex) {
            return accessToken;
        }

    }

    public static String decodeJsonBody(String accessToken) {
        String tokenBody = accessToken.split("\\.")[1];

        byte[] decodedBytes = Base64.getUrlDecoder().decode(tokenBody);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }


}
