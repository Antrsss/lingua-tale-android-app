package com.example.linguatale.data.remote.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface BookApi {

    @GET("books")
    suspend fun getUserBooks(): List<BookDto>

    @GET("books/{id}")
    suspend fun getBook(@Path("id") id: String): BookDto

    @Multipart
    @POST("books")
    suspend fun uploadBook(
        @Part file: MultipartBody.Part,
        @Part("metadata") metadata: RequestBody
    ): BookDto

    @DELETE("books/{id}")
    suspend fun deleteBook(@Path("id") id: String)

    @GET("books/{bookId}/chapters")
    suspend fun getChapters(@Path("bookId") bookId: String): List<ChapterDto>

    @GET("books/{bookId}/chapters/{order}/content")
    suspend fun getChapterContent(
        @Path("bookId") bookId: String,
        @Path("order") order: Int
    ): ChapterContentDto

    @GET("books/{bookId}/chapters/{order}/annotations")
    suspend fun getAnnotations(
        @Path("bookId") bookId: String,
        @Path("order") order: Int
    ): AnnotationsDto
}