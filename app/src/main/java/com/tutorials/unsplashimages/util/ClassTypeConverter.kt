package com.tutorials.unsplashimages.util

import androidx.room.TypeConverter
import com.tutorials.unsplashimages.data.model.ImageLocation
import com.tutorials.unsplashimages.data.model.ImageUrl
import com.tutorials.unsplashimages.data.model.ImageUser
import org.json.JSONObject

class ClassTypeConverter {

    @TypeConverter
    fun fromImageUrl(url: ImageUrl):String{
        val jsonObject = JSONObject()
        jsonObject.put("raw",url.raw)
        jsonObject.put("regular",url.regular)
        return jsonObject.toString()
    }

    @TypeConverter
    fun toImageUrl(data:String): ImageUrl {
        val jsonObject = JSONObject(data)
        val raw = jsonObject.get("raw") as String
        val regular = jsonObject.get("regular") as String
        return ImageUrl(raw = raw,regular = regular)
    }

    @TypeConverter
    fun fromImageLocation(imageLocation: ImageLocation):String{
        return imageLocation.name
    }

    @TypeConverter
    fun toImageLocation(name:String): ImageLocation {
        return ImageLocation(name)
    }
    @TypeConverter
    fun fromImageUser(user: ImageUser):String{
        return user.username
    }

    @TypeConverter
    fun toImageUser(name:String): ImageUser {
        return ImageUser(name)
    }
}