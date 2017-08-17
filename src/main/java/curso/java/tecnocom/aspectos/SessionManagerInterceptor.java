package curso.java.tecnocom.aspectos;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import curso.tecnocom.redsocial.daos.GenericDao;

@Component
@Aspect
public class SessionManagerInterceptor
{
	@Pointcut("execution(@curso.java.tecnocom.anotaciones.SessionManager * *.*(..)  )")
	public void sujetador()
	{

	}

	@Around("sujetador()")
	public Object ponerSesion(ProceedingJoinPoint joinPoint)
	{

		try
		{

			GenericDao genericDao = (GenericDao) joinPoint.getTarget();

			// asi se enchufa la sesion sin preocuparme
			try
			{
				if (genericDao.getSession() == null || !genericDao.getSession().isOpen())
					genericDao.setSession(genericDao.getSessionFactory().openSession());
				else // quitar este else
					genericDao.setSession(genericDao.getSessionFactory().getCurrentSession());
			} catch (Exception e)
			{
				genericDao.setSession(genericDao.getSessionFactory().openSession());
			}

			Object objeto = joinPoint.proceed();
			return objeto;
		} catch (Throwable e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
