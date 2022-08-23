package com.alcantaracarlos.earthquakemonitor;

import com.alcantaracarlos.earthquakemonitor.api.EarthquakeJSONResponse;
import com.alcantaracarlos.earthquakemonitor.api.EqApiClient;
import com.alcantaracarlos.earthquakemonitor.api.Features;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainRepository {

    public interface DownloadEqsListener {
        void onEqsDownloaded(List<Earthquake> earthquakeList);
    }

    public void getEarthquakes(DownloadEqsListener downloadEqsListener){

        EqApiClient.EqService service = EqApiClient.getInstance().getService();

        service.getEarthquakes().enqueue(new Callback<EarthquakeJSONResponse>() {
            @Override
            public void onResponse(Call<EarthquakeJSONResponse> call, Response<EarthquakeJSONResponse> response) {
                // List<Earthquake> earthquakeList = parseEarthquakes(response.body());

                List<Earthquake> earthquakeList = getEarthquakesWithMoshi(response.body());
                downloadEqsListener.onEqsDownloaded(earthquakeList);
            }

            @Override
            public void onFailure(Call<EarthquakeJSONResponse> call, Throwable t) {

            }
        });


    }

    private List<Earthquake> getEarthquakesWithMoshi(EarthquakeJSONResponse body) {
        ArrayList<Earthquake> eqList = new ArrayList<>();

        List<Features> features = body.getFeatures();
        for (Features feature: features ){
            String id = feature.getId();
            double magnitude = feature.getProperties().getMag();
            String place = feature.getProperties().getPlace();
            long time = feature.getProperties().getTime();

            double longitude = feature.getGeometry().getLongitude();
            double latitude = feature.getGeometry().getLatitude();

            Earthquake earthquake = new Earthquake(id, place, magnitude, time, latitude, longitude);
            eqList.add(earthquake);
        }

        return eqList;

    }


}
