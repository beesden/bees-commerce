package org.beesden.commerce.dao.impl;

import org.beesden.commerce.dao.ProductDAO;
import org.beesden.commerce.model.entity.Product;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProductDAOImpl implements ProductDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Product getProductById(String id) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Product.class).createAlias("variants", "variant");
		criteria.add(Restrictions.idEq(id)).setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return (Product) criteria.uniqueResult();
	}

	// public void addProduct(Product p) {
	// Session session = sessionFactory.getCurrentSession();
	// session.persist(p);
	// logger.info("Product saved successfully, Product Details=" + p);
	// }
	//
	// @SuppressWarnings("unchecked")
	// public List<Product> listProducts() {
	// Session session = sessionFactory.getCurrentSession();
	// List<Product> productsList = session.createQuery("from Product").list();
	// for (Product p : productsList) {
	// logger.info("Product List::" + p);
	// }
	// return productsList;
	// }
	//
	// public void removeProduct(int id) {
	// Session session = sessionFactory.getCurrentSession();
	// Product p = (Product) session.load(Product.class, new Integer(id));
	// if (null != p) {
	// session.delete(p);
	// }
	// logger.info("Product deleted successfully, product details=" + p);
	// }
	//
	// public void updateProduct(Product p) {
	// Session session = sessionFactory.getCurrentSession();
	// session.update(p);
	// logger.info("Product updated successfully, Product Details=" + p);
	// }

}