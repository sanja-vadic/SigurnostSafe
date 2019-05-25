package net.etfbl.sanja.ids;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LogMessage {
	private long timestamp;
	private String ipAddress;
	private String requestMethod;
	private AttackType attackType;
	private String data;
	
	@Override
	public String toString() {
		return timestamp + " - " + ipAddress + "\t" + requestMethod + "\t" + attackType + "\t" + data;
	}
	
	public enum AttackType {
		SQLI, XSS, PARAMETER_TEMPERING, HTTP_SLOW
	}
}
