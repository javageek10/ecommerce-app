package com.ecommerceapp.config;

import com.ecommerceapp.entity.Country;
import com.ecommerceapp.entity.Product;
import com.ecommerceapp.entity.ProductCategory;
import com.ecommerceapp.entity.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class DataRestConfig  implements RepositoryRestConfigurer {


    // expose the 'id' -> show it
    private EntityManager entityManager;

    @Autowired
    public DataRestConfig( EntityManager theEntityManager){
        this.entityManager = theEntityManager;
    }
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {

        // disable unsupported actions for products and make it read only

        HttpMethod[] unSupportedActions = {HttpMethod.PUT, HttpMethod.POST,HttpMethod.DELETE};

        // disable unsupported actions for product category and make it read only

        disableHttpMethods(ProductCategory.class,config, unSupportedActions);
        disableHttpMethods(Product.class,config, unSupportedActions);
        disableHttpMethods(Country.class,config, unSupportedActions);
        disableHttpMethods(State.class,config, unSupportedActions);

        exposeIds(config);

    }

    private void disableHttpMethods(Class theClass , RepositoryRestConfiguration config, HttpMethod[] unSupportedActions) {
        config.getExposureConfiguration().forDomainType(theClass)
                .withItemExposure(((metdata, httpMethods) -> httpMethods.disable(unSupportedActions)))
                .withCollectionExposure(((metdata, httpMethods) -> httpMethods.disable(unSupportedActions)));
    }

    private void exposeIds(RepositoryRestConfiguration config) {
        /***
         * expose entity ids
         * get list of all entity classes from entity manaher
         */

        Set<EntityType<?>> entityTypes = entityManager.getMetamodel().getEntities();

        // create an  array of entity type
        List<Class> entityClasses = new ArrayList<>();

        //get the entity types of the entities

        for (EntityType entityType:entityTypes){
            entityClasses.add(entityType.getJavaType());
        }

        // expose the entity ids for an  array
       Class[] domainTypes = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);

    }
}
