
package afdemp_project_individual;

/**
 *
 * @author DStelios
 */
public class Role {
    private boolean view = false;
    private boolean edit = false;
    private boolean delete = false;
    private String name;
    private int id;
    
    public Role(){}
    public Role(String name, boolean view, boolean edit, boolean delete){
        this.name = name;
        this.view = view;
        this.edit = edit;
        this.delete = delete;
    }

    public boolean hasView() { return view; }
    public void setView(boolean view) { this.view = view; }

    public boolean hasEdit() { return edit; }
    public void setEdit(boolean edit) { this.edit = edit; }

    public boolean hasDelete() { return delete; }
    public void setDelete(boolean delete) { this.delete = delete; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    @Override
    public String toString(){
        return "Name:\t" + name + "\nView:\t" + view + 
               "\nEdit:\t" + edit + "\nDelete:\t" + delete + "\nRole ID: " + id; 
    }
 
}
