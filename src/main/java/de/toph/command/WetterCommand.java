package de.toph.command;

import java.awt.Color;
import java.io.IOException;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.toph.Config;
import de.toph.constant.CommandConstant;
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
public class WetterCommand implements ICommand {

    private static WetterCommand command;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(WetterCommand.class);
    
    private Config conf;
    
    private WetterCommand() {
	conf = new Config();
    }
    
    public static WetterCommand getInstance() {
   	if(command == null) {
   	    command = new WetterCommand();
   	}
   	return command;
       }
    
    @Override
    public void runCommand(String type, Object event) {
	switch(type) {
	case CommandConstant.SLASH:
	    runSlashCommand(event);
	    break;
	case CommandConstant.MODALINTERACTION:
	    runModalInteraction(event);
	    break;
	}
    }
    
    private void runModalInteraction(Object event) {
	ModalInteractionEvent mEvent = (ModalInteractionEvent) event;
	String city = mEvent.getValue("weatercity").getAsString();
	
	try {
	    String json = doRequest(city);
	    Integer celsius = parseWeather(json);
	    
	    EmbedBuilder eb = new EmbedBuilder();
	    eb.setTitle("Wetter");
	    eb.setColor(Color.red);
	    eb.addField("Temperatur", String.format("Es ist %s°C in der Ortschaft", celsius), false);
	    
	    mEvent.replyEmbeds(eb.build()).queue();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private void runSlashCommand(Object event) {
	SlashCommandInteractionEvent slashEvent = (SlashCommandInteractionEvent) event;

	TextInput city = TextInput.create("weatercity", "Stadt", TextInputStyle.SHORT).setPlaceholder("City")
		.setMinLength(2).setMaxLength(40).build();

	Modal modal = Modal.create("weather", "Weather").addComponents(ActionRow.of(city)).build();
	slashEvent.replyModal(modal).queue();}

    private Integer parseWeather(String JsonString) {
	String json = JsonString;

	JSONObject jsonObject = new JSONObject(json);
	JSONObject current = jsonObject.getJSONObject("current");
	
	Integer currentTemp = (Integer) current.get("temperature");
	
	LOGGER.debug(String.valueOf(currentTemp));
	
	return currentTemp;
    }
    
    private String doRequest(String option) throws IOException {
	OkHttpClient client = new OkHttpClient();
	
	HttpUrl.Builder urlBuilder = HttpUrl.parse("http://api.weatherstack.com/current").newBuilder();
	urlBuilder.addQueryParameter("access_key", conf.getWeatherKey());
	urlBuilder.addQueryParameter("query", option);
	
	String url = urlBuilder.build().toString();
	
	Request request = new Request.Builder().url(url).build();
	Response response = client.newCall(request).execute();
	
	String reponseString = response.body().string();
	response.close();
	
	return reponseString;
    }
}