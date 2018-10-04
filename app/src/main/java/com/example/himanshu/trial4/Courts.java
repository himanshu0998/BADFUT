package com.example.himanshu.trial4;

public class Courts {

    private String Address;
    private String Lat;
    private String Long;
    private String Name;
    private String Timings;
    private String Type;


    public Courts(){}

    public Courts(String Address,String Lat,String Long,String Name,String Timings,String Type)
    {
        this.Address = Address;
        this.Lat = Lat;
        this.Long = Long;
        this.Name = Name;
        this.Timings = Timings;
        this.Type = Type;
    }

    public String getAddress() {
        return Address;
    }


    public String getLat() {
        return Lat;
    }

    public String getLong() {
        return Long;
    }

    public String getName() {
        return Name;
    }

    public String getTimings() {
        return Timings;
    }

    public String getType() {
        return Type;
    }


}
