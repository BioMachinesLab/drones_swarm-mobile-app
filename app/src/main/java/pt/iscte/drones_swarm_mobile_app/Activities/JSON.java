//
// private void receiveDataJSONSite() {
//
//
//        StrictMode.ThreadPolicy policy = new
//                StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//
//        HttpClient httpclient = new DefaultHttpClient();
//
//        // Prepare a request object
//        HttpGet httpget = new HttpGet(URL_SANTA_RITA);
//
//        // Execute the request
//        HttpResponse response;
//        try {
//            response = httpclient.execute(httpget);
//            // Examine the response status
//            //Log.i("Info",response.getStatusLine().toString());  Comes back with HTTP/1.1 200 OK
//
//            // Get hold of the response entity
//            HttpEntity entity = response.getEntity();
//
//            if (entity != null) {
//                InputStream instream = entity.getContent();
//                String result = AuxiliarJson.convertStreamToString(instream);
//
//                JSONObject obj = new JSONObject(result);
//                textoDestaques = obj.getString("textodestaques");
//                textoAuxiliar = obj.getString("textoauxiliar");
//
//                // Toast.makeText(SantaRitaActivity.this, obj.getString("textodestaques"), Toast.LENGTH_LONG).show();
//                instream.close();
//            }
//
//
//        } catch (Exception e) {
//            Log.e("Error", e.toString());
//        }
//    }
