package com.magestore.app.customer;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class MagestorePosAPI {



    public static String login(String strUserName, String strPassưord) throws IOException {
        // Tạo URL truy vấn API login với username, password
        final String urlLogin = "http://demo-magento2.magestore.com/webpos/rest/default/V1/webpos/staff/login";
        final URL url = new URL(urlLogin);

        // Tạo đối tượng nhận tham số là user name và password
        LoginObject loginObject = new LoginObject();
        loginObject.staff = new User();
        loginObject.staff.username = strUserName;
        loginObject.staff.password = strPassưord;

        // Tạo http connection để thực thi API
        HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
        httpConnection.setDoOutput(true);
        httpConnection.setDoInput(true);
        httpConnection.setRequestProperty("Content-Type", "application/json");
        httpConnection.setRequestProperty("Accept", "application/json");
        httpConnection.setRequestMethod("POST");

        // Chuyển đối tượng tham số thành chuỗi json và truyền vào cho http connection
        Gson gson = new Gson();
        OutputStreamWriter wr = new OutputStreamWriter(httpConnection.getOutputStream());
        gson.toJson(loginObject, wr);
        wr.flush();
        wr.close();

        // thực hiện kết nối http connection, nhận kết quả trả về
        InputStream is = httpConnection.getInputStream();
        String strResult = convertStreamToString(is);

        // đóng các kết nối lại
        is.close();
        is = null;
        httpConnection.disconnect();
        httpConnection = null;

        // Xử lý kết quả trả về
        boolean success = (strResult != null && (!"false\n".equals(strResult)));
        if (success) {
            // đăng nhập thành công trả về sesssion id
            return strResult.trim().replace("\"", "");
        }
        // không thành công trả về null
        else return null;
    }

    public static List<Customer> getListCustomer() throws IOException {
        // Đăng nhập lấy session id, username/password demo/demo123
        String strSessionID = login("demo", "demo123");

        // Tạo URL truy vấn API danh sách khách hàng
        final String urlGetListCustomer = "http://demo-magento2.magestore.com/webpos/rest/default/V1/webpos/customers/search?searchCriteria[current_page]=2&searchCriteria[page_size]=50&session=" + strSessionID;
        final URL url = new URL(urlGetListCustomer);

        // Gọi http connection thực thi API lấy danh sách khách hàng
        HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
        httpConnection.setRequestMethod("GET");
        InputStream is = httpConnection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        // nhận kết quả server trả về bằng dạng JSON
        // chuyển chuỗi JSON nhận được thành đối tượng Customer và Address
        Gson gson = new Gson();
        CustomerList customerList = gson.fromJson(reader, CustomerList.class);

        // đóng các kết nối lại
        reader.close();
        reader = null;
        is.close();
        is = null;
        httpConnection.disconnect();
        httpConnection = null;

        // Trả kết quả danh sách khách hàng
        return customerList.items;
    }

    /**
     * Tạo String Builder từ InputStream
     * @param is
     * @return
     * @throws IOException
     */
    public static StringBuilder convertStreamToStringBuilder(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append('\n');
        }
        return sb;
    }

    /**
     * Tạo String từ InputStream
     * @param is
     * @return
     * @throws IOException
     */
    public static String convertStreamToString(InputStream is) throws IOException {
        return convertStreamToStringBuilder(is).toString();
    }
}
