package com.example.instauser.ImageSlider

data class BannerModel(
    val id: Int,
    val name: String,
    val imageUrl: String
)

val bannerList = listOf(
    BannerModel(1, "Banner 1", "https://picsum.photos/id/1043/5184/3456"),
    BannerModel(2, "Banner 2", "https://picsum.photos/id/1039/6945/4635"),
    BannerModel(3, "Banner 3", "https://picsum.photos/id/1038/3914/5863"),
    BannerModel(4, "Banner 4", "https://picsum.photos/id/1037/5760/3840")
)