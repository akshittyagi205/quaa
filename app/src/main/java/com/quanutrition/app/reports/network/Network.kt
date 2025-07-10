package com.quanutrition.app.reports.network

import com.quanutrition.app.Utils.retrofit
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Query

interface ReportAPIService {

    @GET(Urls.GET_GENETIC_REPORT)
    suspend fun getGeneticReport(
        @HeaderMap headerMap: Map<String, String>
    ): String
    @GET(Urls.GET_MICROBIOME_REPORT)
    suspend fun getMicrobiomicReport(
        @HeaderMap headerMap: Map<String, String>
    ): String
    @GET(Urls.GET_BLOOD_PROGRESS_REPORT)
    suspend fun getBloodPressureReport(
        @HeaderMap headerMap: Map<String, String>
    ): String
}

object ReportAPI {
    val retrofitService: ReportAPIService by lazy {
        retrofit.create(ReportAPIService::class.java)
    }
}