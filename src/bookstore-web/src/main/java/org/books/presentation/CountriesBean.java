/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.books.application.ResourceBundleHelper;
import org.books.persistence.Country;

/**
 * Brean providing the supported countries loaded from the global application
 * configuration.
 *
 * @author micic
 */
@Named("countriesBean")
@SessionScoped
public class CountriesBean implements Serializable {

    private static final String COUNTRIES_CONFIG_KEY = "supportedCountries";

    private static final String LOCALIZATION_PREFIX = "country_";

    private static final String APP_CONFIG_FILE = "/config/globalAppConfig.properties";

    private final List<Country> supportedCountries = new ArrayList<>();

    @PostConstruct
    private void init() {

        // TODO: move application configuration logic to another class
        try (InputStream inputStream = CountriesBean.class.getClassLoader().getResourceAsStream(APP_CONFIG_FILE);) {

            Properties properties = new Properties();
            properties.load(inputStream);

            String countriesConf = properties.getProperty(COUNTRIES_CONFIG_KEY);
            if (countriesConf != null) {
                for (String code : countriesConf.split(",")) {
                    this.supportedCountries.add(new Country(code, codeToString(code)));
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(CountriesBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Country> getSupportedCountries() {
        return this.supportedCountries;
    }

    public String codeToString(String code) {
        return ResourceBundleHelper.getLocalizedText(LOCALIZATION_PREFIX + code.toLowerCase());
    }
}
