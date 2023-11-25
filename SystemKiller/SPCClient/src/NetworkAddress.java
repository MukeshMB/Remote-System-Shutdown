import static java.lang.Math.pow;

public class NetworkAddress {
    private final String IPV4;
    private final short SUBNET;
    private final String BROADCAST;

    NetworkAddress(String IPV4, short SUBNET) {
        this.IPV4 = IPV4;
        this.SUBNET = SUBNET;
        this.BROADCAST = computeBroadcastAddress();
    }

    String getBroadcastAddress() {
        return this.BROADCAST;
    }

    String computeBroadcastAddress() {
        long ip = ipToLong(this.IPV4);
        long res = (long) pow(2, 32-this.SUBNET) - 1;
        for(int i=0; i<this.SUBNET; i++) {
            res |= ip & (1L << (31-i));
        }
        return longToIp(res);
    }

    public static long ipToLong(String ipAddress) {
        String[] octets = ipAddress.split("\\.");
        long result = 0;
        for (int i = 0; i < 4; i++) {
            int octetValue = Integer.parseInt(octets[i]);
            result |= ((long) octetValue) << ((3 - i) * 8);
        }
        return result;
    }

    public static String longToIp(long ip) {
        StringBuilder sb = new StringBuilder(15);
        for (int i = 0; i < 4; i++) {
            int octet = (int) ((ip >> ((3 - i) * 8)) & 0xFF);
            if (i > 0) {
                sb.append('.');
            }
            sb.append(octet);
        }
        return sb.toString();
    }
}
