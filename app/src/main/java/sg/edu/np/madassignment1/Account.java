package sg.edu.np.madassignment1;

public class Account {
    private String username;
    public String getUsername(){ return username; };
    public void setUsername(String newUsername){ this.username = newUsername; };

    private String email;
    public String getEmail(){ return email; };
    public void setEmail(String newEmail){ this.email = newEmail; };

    private String password;
   // public String getPassword(){ return password; };
    //public void setPassword(String newPassword){ this.password = newPassword; };


    public Account() {
    }

    public Account(String u, String e, String p){
        this.username = u;
        this.email = e;
        this.password = p;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
