package com.example.data

/**
 * Configuration file for external APIs.
 * Since this application is designed to be fully offline, these keys are documented here
 * but the actual Data Ingestion Module loads an offline JSON snapshot to prevent live network calls.
 *
 * If you ever wish to enable real-time fetching, you can use these keys with Retrofit.
 */
object ExternalApiConfig {
    const val FATSECRET_CLIENT_ID = "YOUR_FATSECRET_CLIENT_ID"
    const val FATSECRET_CLIENT_SECRET = "YOUR_FATSECRET_CLIENT_SECRET"
    
    const val USDA_API_KEY = "YOUR_USDA_API_KEY"
    
    // Future expansion for FooDB or Kaggle remote fetching
    const val KAGGLE_USERNAME = "YOUR_KAGGLE_USERNAME"
    const val KAGGLE_KEY = "YOUR_KAGGLE_KEY"
}
