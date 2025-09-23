# 🌍 SkyGrid App  

**SkyGrid** is an **AI-powered Android application** that integrates **geospatial APIs** for  
**Flood Detection**, 🛰️ **Object Recognition**, and **Land Analysis** using **satellite imagery**.  
Designed for **smart disaster management, environmental monitoring, and land-use planning**.  

---

## Features
- **Flood Analysis** – Detect and visualize flood-affected areas using geospatial flood datasets.  
- **Object Recognition** – Identify objects (buildings, vehicles, infrastructure) from aerial/satellite images.  
- **Land Analysis** – Classify land cover, vegetation, and urban expansion for better planning.    
- **User-Friendly Android App** – Lightweight, responsive, and designed for both offline & online use.  

---

## Tech Stack
**Frontend (App)**  
- Android (Java/Kotlin)  
- Retrofit (API client)  
- Material UI  

**Backend / AI Integration**  
- FastAPI (Python)  
- PyTorch / TensorFlow (AI models)  
- Hugging Face Models (e.g., Prithvi Flood Segmentation, Object Detection APIs)  

**Data & APIs**  
- **Satellite Imagery** (Sentinel, Landsat, UAV data)  
- **Flood APIs** – Sen1Floods11 / Prithvi Flood Model  
- **Land APIs** – Land cover classification datasets  
- **Object Detection APIs** – Pre-trained object recognition models  

---

##  Implementation Procedure
1. **Setup Android Project**  
   - Create Android Studio project  
   - Add Retrofit for API communication  
   - Configure permissions for internet & storage  

2. **Integrate APIs**  
   - Connect **Flood Detection API** for analyzing flooded regions  
   - Use **Land Cover API** for land classification  
   - Connect **Object Detection API** for identifying infrastructure & resources  

3. **Backend (FastAPI)**  
   - Build REST APIs to handle image upload & processing  
   - Load AI models (flood segmentation, land classification, object detection)  
   - Send processed results to the Android app  

---

## Use Cases
- **Disaster Management** – Real-time flood monitoring & damage assessment  
- **Agriculture** – Crop health & land usage analysis  
- **Urban Planning** – Infrastructure monitoring & development planning  
- **Environmental Studies** – Deforestation tracking & climate impact  

---

## 📸 Screenshots
<img width="1687" height="566" alt="image" src="https://github.com/user-attachments/assets/875334c8-53ec-4eca-b6b4-c64805f15230" />
 

---

## Future Enhancements
- AI-powered **predictive flood mapping**  
- **Drone integration** for real-time aerial imagery  
- Cloud-based **data pipelines** for faster processing  

---

## Contributing
Contributions are welcome! Please open an issue or submit a pull request for improvements.  

---

## License
This project is licensed under the **MIT License**.  

---

### Built with  using **AI + Satellite Imagery + Android**
