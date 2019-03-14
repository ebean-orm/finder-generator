package org.example.domain.query;

import io.ebean.Database;
import io.ebean.typequery.PLong;
import io.ebean.typequery.PString;
import io.ebean.typequery.PTimestamp;
import io.ebean.typequery.TQRootBean;
import io.ebean.typequery.TypeQueryBean;
import org.example.domain.Address;
import org.example.domain.query.assoc.QAssocCountry;

/**
 * Query bean for Address.
 * 
 * THIS IS A GENERATED OBJECT, DO NOT MODIFY THIS CLASS.
 */
@TypeQueryBean
public class QAddress extends TQRootBean<Address,QAddress> {

  private static final QAddress _alias = new QAddress(true);

  /**
   * Return the shared 'Alias' instance used to provide properties to 
   * <code>select()</code> and <code>fetch()</code> 
   */
  public static QAddress alias() {
    return _alias;
  }

  public PLong<QAddress> id;
  public PLong<QAddress> version;
  public PTimestamp<QAddress> whenCreated;
  public PTimestamp<QAddress> whenUpdated;
  public PString<QAddress> line1;
  public PString<QAddress> line2;
  public PString<QAddress> city;
  public QAssocCountry<QAddress> country;


  /**
   * Construct with a given Database.
   */
  public QAddress(Database database) {
    super(Address.class, database);
  }

  /**
   * Construct using the default Database.
   */
  public QAddress() {
    super(Address.class);
  }

  /**
   * Construct for Alias.
   */
  private QAddress(boolean dummy) {
    super(dummy);
  }
}
