package domain;

public class Workout implements Identifiable{
    private String id;
    private int start;
    private int end;
    private String name;
    private int intensity;
    private String desc;


    public Workout(String id, int start, int end, String name, int intensity, String desc){
        this.id = id;
        this.start = start;
        this.desc = desc;
        this.end = end;
        this.intensity = intensity;
        this.name = name;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public void setID(String id) {
        this.id = id;
    }


    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString(){
        return id + " " + start + ";" + end + ";" + name + ";" + intensity + ";" +desc;
    }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }
}
