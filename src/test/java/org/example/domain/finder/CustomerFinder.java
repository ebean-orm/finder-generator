package org.example.domain.finder;

import com.avaje.ebean.Finder;
import org.example.domain.Customer;
import org.example.domain.query.QCustomer;

public class CustomerFinder extends Finder<Long,Customer> {

  /**
   * Construct using the default EbeanServer.
   */
  public CustomerFinder() {
    super(Customer.class);
  }

  /**
   * Construct with a given EbeanServer.
   */
  public CustomerFinder(String serverName) {
    super(serverName, Customer.class);
  }

  /**
   * Start a new typed query.
   */
  protected QCustomer where() {
     return new QCustomer();
  }
}
