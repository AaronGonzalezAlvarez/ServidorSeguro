package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import coneccion.Coneccion;
import models.Coordenada;
import models.Credenciales;
import models.User;
public class GameService extends Coneccion{
	
	PreparedStatement ps;
    ResultSet rs;
    Object o1 = new Object();
	
	public ArrayList<Integer> listGameFinalized(){
		synchronized (o1) {
			ArrayList<Integer> list = null;
			Connection conexion = getConnection();
	        String sql = "SELECT id FROM partida WHERE state='FINALIZADA';";
	        try {
	            Statement s = conexion.createStatement();
	            ResultSet resultado = s.executeQuery(sql);
	            list = new ArrayList<Integer>();
	            while(resultado.next()){
	            	list.add(resultado.getInt(1));
	            }
	            s.close();
	            return list;
	        } catch (Exception ex) {
	            System.out.println("error desde el modelo: " + ex);
	            return list;
	        } finally {
	            try {
	                conexion.close();
	                
	            } catch (Exception ex) {
	                System.out.println("error desde el modelo: " + ex);
	            }
	        }
		}		        
	}
	
	public ArrayList<Integer> listGameUserFinalized(String nick){
		synchronized (o1) {
			ArrayList<Integer> list = null;
			Connection conexion = getConnection();
	        String sql = "SELECT partida_id FROM userspartida us INNER JOIN partida pa ON us.partida_id=pa.id where  us.user_nick ='"+nick+"' AND pa.state='FINALIZADA' order by us.id desc;";
	        try {
	            Statement s = conexion.createStatement();
	            ResultSet resultado = s.executeQuery(sql);
	            list = new ArrayList<Integer>();
	            while(resultado.next()){
	            	list.add(resultado.getInt(1));
	            }
	            s.close();
	            return list;
	        } catch (Exception ex) {
	            System.out.println("error desde el modelo: " + ex);
	            return list;
	        } finally {
	            try {
	                conexion.close();
	                
	            } catch (Exception ex) {
	                System.out.println("error desde el modelo: " + ex);
	            }
	        }
		}		        
	}
	
	public ArrayList<Coordenada> bringBoard (String nick, String idGame){
			synchronized (o1) {
				ArrayList<Coordenada> list = null;
				Connection conexion = getConnection();
		        String sql = "SELECT ejeX,ejeY FROM boats WHERE user_nick='"+nick+"' AND partida_id="+idGame+";";
		        try {
		            Statement s = conexion.createStatement();
		            ResultSet resultado = s.executeQuery(sql);
		            list = new ArrayList<Coordenada>();
		            while(resultado.next()){
		            	list.add(new Coordenada(resultado.getInt(1),resultado.getInt(2),'B'));
		            }
		            s.close();
		            return list;
		        } catch (Exception ex) {
		            System.out.println("error desde el modelo: " + ex);
		            return list;
		        } finally {
		            try {
		                conexion.close();
		                
		            } catch (Exception ex) {
		                System.out.println("error desde el modelo: " + ex);
		            }
		        }
			}
		}

	public ArrayList<Coordenada> myShots(String idPartida, String nick) {
			synchronized (o1) {
				ArrayList<Coordenada> list = null;
				Connection conexion = getConnection();
		        String sql = "SELECT ejeX,ejeY,tipo FROM movements WHERE user_nick='"+nick+"' AND partida_id="+idPartida+";";
		        try {
		            Statement s = conexion.createStatement();
		            ResultSet resultado = s.executeQuery(sql);
		            list = new ArrayList<Coordenada>();
		            while(resultado.next()){
		            	String t= resultado.getString(3);
		            	list.add(new Coordenada(resultado.getInt(1),resultado.getInt(2), t.charAt(0)));
		            }
		            s.close();
		            return list;
		        } catch (Exception ex) {
		            System.out.println("error desde el modelo: " + ex);
		            return list;
		        } finally {
		            try {
		                conexion.close();
		                
		            } catch (Exception ex) {
		                System.out.println("error desde el modelo: " + ex);
		            }
		        }
			}
		}
	
	public ArrayList<User> listJugadores (String idGame){		
		synchronized (o1) {
			ArrayList<User> list = null;
			Connection conexion = getConnection();
	        String sql = "SELECT user_nick, winner FROM userspartida WHERE partida_id="+idGame+";";
	        try {
	            Statement s = conexion.createStatement();
	            ResultSet resultado = s.executeQuery(sql);
	            list = new ArrayList<User>();
	            while(resultado.next()){
	            	list.add(new User(resultado.getString(1),resultado.getString(2)));
	            }
	            s.close();
	            return list;
	        } catch (Exception ex) {
	            System.out.println("error desde el modelo: " + ex);
	            return list;
	        } finally {
	            try {
	                conexion.close();
	                
	            } catch (Exception ex) {
	                System.out.println("error desde el modelo: " + ex);
	            }
	        }
		}
	}
	
	public boolean checkLogin(Credenciales credenciales) {
		synchronized (o1) {
			Connection conexion = getConnection();
			try {
				ps = conexion.prepareStatement("SELECT * FROM users WHERE nick = ? AND PASSWORD= ?;");
				ps.setString(1, credenciales.getNick());
				ps.setString(2, credenciales.getPassword());
				rs = ps.executeQuery();

				if (rs.next()) {
					return true;
				} else {
					return false;
				}

			} catch (Exception ex) {
				System.out.println("error desde el modelo: " + ex);
				return false;
			} finally {
				try {
					conexion.close();
				} catch (Exception ex) {
					System.out.println("error desde el modelo: " + ex);
				}
			}
		}
	}
}
