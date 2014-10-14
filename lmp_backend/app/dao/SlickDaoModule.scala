package dao.slick

import dao.Dao
import com.google.inject.AbstractModule
import dao.SlickDao

object SlickDaoModule extends AbstractModule {

  /**
   * Configure Guice bindings for the data-access layer.
   */
  override def configure = {
    bind(classOf[Dao]).to(classOf[SlickDao])
  }

}