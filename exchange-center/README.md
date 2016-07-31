cash accounts stored in mysql;
security accounts stored in sqlite/remote mysql;
both mysql and sqlite/remote mysql are transaction supported;

cash account accessed by spring data jpa;
security account accessed by mybatis;

sqlite sounds like not jta supported so create two  databases mysql instead;

exchangeone
 store cash accounts
exchangetwo
 store security accounts