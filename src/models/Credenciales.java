package models;


public class Credenciales {

	String nick;
	String password;
	
	public Credenciales(String nick, String password) {
		this.nick = nick;
		this.password = password;
	}
	
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
