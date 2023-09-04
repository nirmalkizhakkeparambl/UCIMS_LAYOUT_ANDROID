package com.gisfy.unauthorizedlayouts.WMS_Layer;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

public class TileProviderFactory {
	
	public static WMSTileProvider getOsgeoWmsTileProvider() {


        //This is configured for:
        // http://beta.sedac.ciesin.columbia.edu/maps/services
        // (TODO check that this WMS service still exists at the time you try to run this demo,
        // if it doesn't, find another one that supports EPSG:900913
        final String WMS_FORMAT_STRING =
				"http://148.72.208.177:8085/geoserver/cite/wms?service=WMS&version=1.1.0&request=GetMap&layers=cite%3AtblUnauthorizedConstruction&bbox=-0.402830541133881%2C-0.402830541133881%2C80.9689407348633%2C80.9689407348633&width=768&height=768&srs=EPSG%3A4326&format=application/openlayers";
		
		
		WMSTileProvider tileProvider = new WMSTileProvider(256,256) {
        	
	        @Override
	        public synchronized URL getTileUrl(int x, int y, int zoom) {
	        	double[] bbox = getBoundingBox(x, y, zoom);
	            String s = String.format(Locale.UK, WMS_FORMAT_STRING, bbox[MINX],
	            		bbox[MINY], bbox[MAXX], bbox[MAXY]);
	            Log.d("WMSDEMO", s);
	            URL url = null;
	            try {
	                url = new URL(s);
	            } catch (MalformedURLException e) {
	                throw new AssertionError(e);
	            }
	            return url;
	        }
		};
		return tileProvider;
	}
}
