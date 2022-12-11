package de.toph.service;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.toph.DiscordBot;
import de.toph.listener.ClearMessageListener;
import de.toph.listener.MessageOfMonthListener;
import de.toph.listener.MoveAllListener;
import de.toph.listener.RoleListener;
import de.toph.listener.TempVoiceListener;
import de.toph.listener.VerifyListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * 
 * @author Toph
 *
 */
public class ServiceImpl implements IService {

    private static final Logger logger = LoggerFactory.getLogger(ServiceImpl.class);

    /**
     * Set up new Events
     * 
     * @return events
     */
    private List<ListenerAdapter> getEvents() {
	List<ListenerAdapter> events = new ArrayList<>();
	// L I S T E N E R
	events.add(new TempVoiceListener());
	events.add(new RoleListener());

	// S L A S H
	events.add(new MessageOfMonthListener());
	events.add(new VerifyListener());
	events.add(new MoveAllListener());
	events.add(new ClearMessageListener());

	return events;
    }

    /**
     * append the events and build the jda
     */
    @Override
    public JDA getBuilderWithEventListener() {
	try {
	    List<ListenerAdapter> events = getEvents();
	    JDABuilder builder = DiscordBot.getInstance().getBuilder();
	    for (ListenerAdapter event : events) {
		builder.addEventListeners(event);
	    }
	    return builder.build();
	} catch (LoginException e) {
	    logger.error(e.getMessage(), e);
	}
	return null;
    }

}
