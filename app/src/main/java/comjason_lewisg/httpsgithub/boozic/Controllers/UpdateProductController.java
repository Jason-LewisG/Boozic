package comjason_lewisg.httpsgithub.boozic.Controllers;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import comjason_lewisg.httpsgithub.boozic.ProductActivity;

public class UpdateProductController {

    public void onCreate() {}

    public UpdateProductController() {}

    public void updateProduct(ProductActivity p) {
        getProductInBackground(p);
    }

    private void getProductInBackground(final ProductActivity p) {

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... urls) {
                try {
                    StringBuilder urlString = new StringBuilder();
                    //TODO: Store the Server IP in global locaiton
                    urlString.append("http://54.210.175.98:9080/api/products/updateProduct?");
                    //append productID
                    urlString.append("ProductId=").append(p.updatedModel.productId);
                    //if store information was updated, append store info
                    if (p.updatedModel.Price != -1 && p.updatedModel.StoreID != -1) {
                        urlString.append("&StoreId=").append(p.updatedModel.StoreID);
                        urlString.append("&Price=").append(p.updatedModel.Price);
                    }
                    //append productName
                    if (p.updatedModel.label != null) {
                        String value = URLEncoder.encode(p.updatedModel.label, "UTF-8");
                        urlString.append("&ProductName=").append(value);
                    }
                    //append productID
                    if (p.updatedModel.type != -1) urlString.append("&ProductTypeId=").append(p.updatedModel.type);
                    //append ABV
                    if (p.updatedModel.abv != -1) urlString.append("&ABV=").append(p.updatedModel.abv);
                    //append volume if changed
                    if (p.updatedModel.volume != -1) urlString.append("&Volume=").append(p.updatedModel.volume);
                    //append units
                    if (p.updatedModel.volumeMeasure != null) urlString.append("&VolumeUnit=").append(p.updatedModel.volumeMeasure);
                    //append container
                    if (p.updatedModel.containerType != null) {
                        String container = URLEncoder.encode(p.updatedModel.containerType, "UTF-8");
                        urlString.append("&ContainerType=").append(container);
                    }
                    //append container quantity
                    if (p.updatedModel.containerQuantity != -1) urlString.append("&ContainerQty=").append(p.updatedModel.containerQuantity);

                    if (p.updatedModel.userRating != -1 || p.updatedModel.favorite != -1) {
                        String android_id = "" + android.provider.Settings.Secure.getString(p.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                        urlString.append("&DeviceId=").append(android_id);
                        //append user rating
                        if (p.updatedModel.userRating != -1) {
                            urlString.append("&Rating=").append((int)p.updatedModel.userRating);
                        }
                        //append favorite
                        if (p.updatedModel.favorite != -1) {
                            urlString.append("&addToFavouritesList=").append(p.updatedModel.favorite);
                        }
                    }

                    Log.v("updateProductURL", urlString.toString());
                    URL url = new URL(urlString.toString());
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");
                        }
                        bufferedReader.close();
                        return stringBuilder.toString();
                    } finally {
                        urlConnection.disconnect();
                    }
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage(), e);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String response) {
                if (response == null) {
                    Log.v("ERROR", "There was an error");
                }
            }
        }.execute();
    }
}
