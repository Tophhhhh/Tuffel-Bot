package de.toph.command;

import java.awt.Color;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.toph.Config;
import de.toph.exception.WeatherException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 
 * @author Tophhhhh
 *
 */
public class WeatherCommand extends AbstractCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherCommand.class);
    
    @Override
    protected void runModalInteraction(ModalInteractionEvent event) {
	String city = event.getValue("weathercity").getAsString();
	
	try {
	    String json = doRequest(city);
	    Integer celsius = parseWeather(json);
	    
	    String message = String.format("Es ist %s°C in %s", celsius, city);
	    String error = "Die eingegebene Stadt ist ungültig!";
	    
	    EmbedBuilder eb = new EmbedBuilder();
	    eb.setTitle("Wetter");
	    eb.setColor(Color.red);
	    eb.addField("Temperatur", celsius == null ? error : message, false);
	    
	    event.replyEmbeds(eb.build()).queue();
	} catch (IOException | WeatherException e) {
	    LOGGER.error(e.getMessage(), e);
	}
    }

    @Override
    protected void runSlashCommand(SlashCommandInteractionEvent event) {
	TextInput city = TextInput.create("weathercity", "Stadt", TextInputStyle.SHORT).setPlaceholder("City")
		.setMinLength(2).setMaxLength(40).build();

	Modal modal = Modal.create("weather", "Weather").addComponents(ActionRow.of(city)).build();
	event.replyModal(modal).queue();
    }

    private Integer parseWeather(String JsonString) {
	String json = JsonString;

	JSONObject jsonObject = new JSONObject(json);
	Integer currentTemp = null;
	try {
	    JSONObject current = jsonObject.getJSONObject("current");
	    currentTemp = (Integer) current.get("temperature");
	    LOGGER.debug(String.valueOf(currentTemp));
	} catch (JSONException e) {
	    LOGGER.error(e.getMessage(), e );
	}
	return currentTemp;
    }
    
    private String doRequest(String option) throws IOException, WeatherException {
	OkHttpClient client = new OkHttpClient();

	Config conf = Config.getInstance();
	
	HttpUrl.Builder urlBuilder = HttpUrl.parse("http://api.weatherstack.com/current").newBuilder();
	urlBuilder.addQueryParameter("access_key", conf.getWeatherKey());
	urlBuilder.addQueryParameter("query", option);
	
	String url = urlBuilder.build().toString();
	
	Request request = new Request.Builder().url(url).build();
	Response response = client.newCall(request).execute();
	
	if(response.code() != 200) {
	    LOGGER.error("HTTP Status is {}. Something went wrong", response.code());
	    throw new WeatherException("could not get response");
	}
	
	String reponseString = response.body().string();
	response.close();
	
	return reponseString;
    }
}