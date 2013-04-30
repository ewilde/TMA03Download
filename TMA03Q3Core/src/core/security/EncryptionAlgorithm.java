package core.security;

/**
 *
 * @author Edward Wilde
 */
public enum EncryptionAlgorithm {
    PBE("PBE", "Password based encryption using DES"),
    AES("AES", "Advanced Encryption Standard as specified by NIST in a draft FIPS. Based on the Rijndael algorithm by Joan Daemen and Vincent Rijmen, AES is a 128-bit block cipher supporting keys of 128, 192, and 256 bits."),
    Blowfish("Blowfish", "The block cipher designed by Bruce Schneier."),
    DES("DES", "The Digital Encryption Standard as described in FIPS PUB 46-2.");

    private final String name;
    private final String description;

    private EncryptionAlgorithm(String name, String description)
    {
        this.name = name;
        this.description = description;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }

    public static String listEncryptionAlgorithms()
    {
        StringBuilder builder = new StringBuilder();
        for(EncryptionAlgorithm algorithm : values())
        {
            builder.append(String.format("%-12s : %s%n",
                    algorithm.getName(), algorithm.getDescription()));
        }

        return builder.toString();
    }
}
