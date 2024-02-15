package com.brandon.playvideo_app.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.brandon.playvideo_app.data.local.VideosDatabase
import com.brandon.playvideo_app.data.local.dao.ChannelDAO
import com.brandon.playvideo_app.data.local.dao.VideoDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context):VideosDatabase =
        Room.databaseBuilder(context, VideosDatabase::class.java, "videos.db")
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    db.execSQL("insert into videos (id, title, description, channelTitle, views, url, isFavorite) values (1, '짜장면이 세상을 구한다', '10억 #짜장면 #엔트로피 똘똘똘이 마트 :: https://ddol.fanshop.me/ ', '똘똘똘이의 유튜브', 12345, 'https://i.ytimg.com/vi/JZPZ7CmKo-I/hqdefault.jpg' , true);")
                    db.execSQL("insert into videos (id, title, description, channelTitle, views, url, isFavorite) values (2, '똘삼은 타르코프가 제일 걱정이다', '패키지게임 #멀티게임 #수익모델 똘똘똘이 마트 :: https://ddol.fanshop.me/ ', '똘똘똘이의 유튜브', 23456, 'https://i.ytimg.com/vi/1epmp84dhj0/hqdefault.jpg' , true);")
                    db.execSQL("insert into videos (id, title, description, channelTitle, views, url, isFavorite) values (3, '차로 온리업하기', '온리업 #빔앤지드라이버 #6축 똘똘똘이 마트 :: https://ddol.fanshop.me/ ', '똘똘똘이의 유튜브', 34567, 'https://i.ytimg.com/vi/cx_o_Qz7GPs/hqdefault.jpg' , true);")
                    db.execSQL("insert into videos (id, title, description, channelTitle, views, url, isFavorite) values (4, '똘똘마트가 팬샵으로 바뀌었습니다!', '팬샵 #국밥 #볶음밥 똘똘똘이 마트 :: https://ddol.fanshop.me/ 국밥EZ 이벤트 상품 바로구매 :: https://fanshop.me/surl/O/27 *바로구매 ...', '똘똘똘이의 유튜브', 45678, 'https://i.ytimg.com/vi/e9Qx2zSCmrk/hqdefault.jpg' , true);")
                    db.execSQL("insert into videos (id, title, description, channelTitle, views, url, isFavorite) values (5, '저번에 온 거 그새 다 먹은 사람', '팬샵 #할인 #볶음밥 똘똘똘이 마트 :: https://ddol.fanshop.me/ ', '똘똘똘이의 유튜브', 56789, 'https://i.ytimg.com/vi/8GksYeQg_Ms/hqdefault.jpg' , true);")
                    db.execSQL("insert into channels (id, channelId, channelTitle, description, url) values (1, '@kia', 'KIA 타이거즈 - 갸티비', '팬 여러분의 많은 응원 부탁드립니다. *영상과 관련이 없거나, 근거 없는 루머/욕설/비난/도배 댓글은 댓글 삭제 또는 ID 차단이 됩니다. 모두가 즐거운 갸티비를 만들어주세요. *비시즌에는 매주 수요일 & 일요일에 콘텐츠를 올려드립니다', 'https://yt3.ggpht.com/gQ52beknf0_4c5bLpvJnJhin0vmKMUgowKJQiWXtZUYkS-wx3hr74odlBII9uJGYfPRA9FT3=s176-c-k-c0x00ffffff-no-rj-mo');")
                    db.execSQL("insert into channels (id, channelId, channelTitle, description, url) values (2, '@eat', '먹어볼래TryToEat', '먹방', 'https://yt3.googleusercontent.com/ytc/AIf8zZQZgYuIQ5WOv03YKci41OFtUYIrj8G8Kysstoa78Q=s176-c-k-c0x00ffffff-no-rj-mo');")
                    db.execSQL("insert into channels (id, channelId, channelTitle, description, url) values (3, ' @lijulike', '리쥬라이크 LIJULIKE', '쥬디바 여러분 설연휴는 잘 보내셨나요?\\n연휴의 마무리는 역시 리쥬라이크❤\uFE0F', 'https://yt3.ggpht.com/pcFLVUDQVSlOjqLjCr3K34RiAvvtj0DGTxbdOzYYqmBnmfKN0vfVRv4FSSJo0X9UnTGEoRcg97U=s176-c-k-c0x00ffffff-no-rj-mo');")
                    db.execSQL("insert into channels (id, channelId, channelTitle, description, url) values (4, ' @ddolddolddol', '똘똘똘이의 유튜브', '팬샵 #국밥 #볶음밥 똘똘똘이 마트 :: https://ddol.fanshop.me/ ', 'https://yt3.googleusercontent.com/ytc/AIf8zZQq8riv-JZ5QK0PzGIbnPcC5SvM6gcJ0flexNwH=s176-c-k-c0x00ffffff-no-rj-mo');")
                    db.execSQL("insert into channels (id, channelId, channelTitle, description, url) values (5, '@hanyeseul', '한예슬 is', '한예슬의 무엇이든 물어보슬\uD83D\uDD2E', 'https://yt3.ggpht.com/lkUkWXjhf7KiLf6VlIblJGDAiL6mFoHIOHa7Z54O6NoKdmvS8hSI2J8ZxqhlUHvuUoUNVwte4Qs=s176-c-k-c0x00ffffff-no-rj-mo');")
                }
            })
            .build()

    @Provides
    fun providesVideosDao(dataBase: VideosDatabase): VideoDAO = dataBase.videoDAO()

    @Provides
    fun providesChannelsDao(dataBase: VideosDatabase): ChannelDAO = dataBase.channelDAO()

}