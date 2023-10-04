package de.toph.tuffelbot.service;

import de.toph.tuffelbot.exception.WeatherException;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WeatherServiceImpl implements WeatherService {

	private final Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);

	@Value("${dc.weather.token}")
	private String token;

	@Override
	public void doRequest(String city) {
		OkHttpClient client = new OkHttpClient();

		HttpUrl.Builder urlBuilder = HttpUrl.parse("http://api.weatherstack.com/current").newBuilder();
		urlBuilder.addQueryParameter("access_key", token);
		urlBuilder.addQueryParameter("query", city);

		String url = urlBuilder.build().toString();

		Request request = new Request.Builder().url(url).build();
		try(Response response = client.newCall(request).execute()) {
			if(response.code() != 200) {
				logger.error("HTTP status is {}.", response.code());
				throw new WeatherException("Wrong HTTP Code!");
			}
			assert response.body() != null;
			String repsonseString = response.body().string();

			// Parser

		} catch (WeatherException | IOException e) {
			logger.error("Something went wrong in {} class", getClass());
		}

	}
}
