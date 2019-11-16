/**
 * Copyrigt (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package dev.examples.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.context.annotation.Description;

import com.github.tools.annotations.api.FieldDescriber;

import dev.examples.models.User;

/**
 * @author wuheng
 * @since 2019.4.10
 * 
 * http://www.mybatis.org/mybatis-3/java-api.html
 *
 */

@Mapper
public interface UserMapper {

	 @Select("select * from user")
    public List<User> retrieveAllUsers();
                                                                                                                                                                                                                                   
    @Select("select * from user where id=#{id}")
    public User retrieveUserById(int id);
                                                                                                                                                                                                                                   
    @Select("select * from user where id=#{id} and name like #{name}")
    public User retrieveUserByIdAndName(int id, String names);
                                                                                                                                                                                                                                   
    @Insert("INSERT INTO user(name, pwd, age) VALUES(#{name},"
            + "#{pwd}, #{age})")
    public void addNewUser(User user);
                                                                                                                                                                                                                                   
    @Delete("delete from user where id=#{id}")
    public void deleteUser(int id);
                                                                                                                                                                                                                                   
    @Update("update user set age=#{age} where name=#{name}")
    public void updateUser(User user);

}
