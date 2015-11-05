package comjason_lewisg.httpsgithub.boozic.Models;

public class ProductStorageModel {
    public String label;
    public String lastUpdate;
    public double userRating;
    public int boozicScore;

    public String upc;
    public int productId;

    public int closestStoreId;
    public int cheapestStoreId;
    public String closestStoreName;
    public String cheapestStoreName;
    public String closestStoreAddress;
    public String cheapestStoreAddress;
    public double closestStoreDist;
    public double cheapestStoreDist;
    public double closestPrice;
    public double cheapestPrice;

    public int typePic;
    public boolean favorite;

    public String container;
    public double volume;
    public String volumeMeasure;

    public double pbv;
    public double abv;
    public int proof;
    public double abp;
    public double pdd;
    public double td;

    public int[] rating = new int[5];
    public double avgRating;

    public ProductStorageModel(String label, String upc, int productId, String lastUpdate, double userRating, int closestStoreId, int cheapestStoreId, String closestStoreName, String cheapestStoreName,
                               String closestStoreAddress, String cheapestStoreAddress, double closestStoreDist, double cheapestStoreDist, double closestPrice, double cheapestPrice, int type, boolean favorite, String container,
                               double abv, int proof, int[] rating, double volume, String volumeMeasure, double pbv, double abp, double pdd, double td, double avgRating) {

        this.label = label;
        this.lastUpdate = lastUpdate;
        this.userRating = userRating;
        typePic = type;

        this.upc = upc;
        this.productId = productId;

        this.closestStoreId = closestStoreId;
        this.cheapestStoreId = cheapestStoreId;
        this.closestStoreName = closestStoreName;
        this.cheapestStoreName = cheapestStoreName;
        this.closestStoreAddress = closestStoreAddress;
        this.cheapestStoreAddress = cheapestStoreAddress;
        this.closestStoreDist = closestStoreDist;
        this.cheapestStoreDist = cheapestStoreDist;
        this.closestPrice = closestPrice;
        this.cheapestPrice = cheapestPrice;
        this.favorite = favorite;
        this.container = container;
        this.abv = abv;
        this.proof = proof;
        System.arraycopy(rating,0,this.rating,0,rating.length);

        this.volume = volume;
        this.volumeMeasure = volumeMeasure;
        this.pbv = pbv;
        this.abp = abp;
        this.pdd = pdd;
        this.td = td;
        this.avgRating = avgRating;
    }
}
