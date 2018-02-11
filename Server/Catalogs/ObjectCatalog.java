package Server.Catalogs;

import java.io.Serializable;
import java.util.ArrayList;
import Server.Models.Object;
public class ObjectCatalog implements Serializable{
    private ArrayList<Object> objects = new ArrayList<Object>();

    public Object makeObject(String name, String status) {
        int id = 0;
        if(objects.size()>0){
            id = objects.get(objects.size() - 1).getObjectId() +1;
        }
        Object newObject = new Object(name, id, status);
        objects.add(newObject);
        return newObject;
    }

    public void removeObject(Object object) {
        objects.remove(object);
    }

    public ArrayList<Object> getObjects() {
        return objects;
    }

    public void setObjects(ArrayList<Object> objects) {
        this.objects = objects;
    }

    public Object getObject(int objectId){
        for (int i = 0; i < objects.size(); i++){
            if(objects.get(i).getObjectId() == objectId){
                return  objects.get(i);
            }
        }
        return null;
    }

}