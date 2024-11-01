function toggleNavbar() {
    const navItems = document.querySelector('.nav-items');
    navItems.classList.toggle('show');
 }
 
 let type= document.getElementById("type");
 let price= document.getElementById("price");
 let homevisit =document.getElementById("homevisit");
 let boarding =document.getElementById("boarding");
 
 {/* <i class="fa-solid fa-circle-info"></i>               alert  */}
 
 function namecheck(){
    let nameip= document.getElementById("name").value;
    let nameerr= document.getElementById("nameerr");
 
    if(nameip.length==0){
       nameerr.innerHTML='<i class="fa-solid fa-circle-info"></i> Name is Required';
       nameerr.style.cssText="color:rgb(208, 43, 43); font-size:15px";
       return false;
    }
    if(!nameip.match(/^[A-Za-z]+\s[A-Za-z]+$/)){
       nameerr.innerHTML='<i class="fa-solid fa-circle-info"></i> Full Name';
       nameerr.style.cssText="color:rgb(208, 43, 43); font-size:15px";
       return false;
   }
   nameerr.innerHTML='<i class="fa-solid fa-circle-check" style="color:green"></i>';
     return true;
 }
 
 function phonecheck(){
    let phoneip= document.getElementById("phone").value;
    let phoneerr= document.getElementById("phoneerr");
 
    if(phoneip.length==0){
       phoneerr.innerHTML='<i class="fa-solid fa-circle-info"></i> Phone No. is Required';
       phoneerr.style.cssText="color:rgb(208, 43, 43); font-size:15px";
       return false;
    }
    if(phoneip.match(/[A-Za-z]$/) || phoneip.length>12 ){
       phoneerr.innerHTML='<i class="fa-solid fa-circle-info"></i> Enter Number';
       phoneerr.style.cssText="color:rgb(208, 43, 43); font-size:15px";
       return false;
    }
    
    if(phoneip.length<10){
       phoneerr.innerHTML='<i class="fa-solid fa-circle-info"></i> 10 digit';
       phoneerr.style.cssText="color:rgb(208, 43, 43); font-size:15px";
       return false;
    }
    if(phoneip.length>12){
       phoneerr.innerHTML='<i class="fa-solid fa-circle-info"></i> Invalid ';
       phoneerr.style.cssText="color:rgb(208, 43, 43); font-size:15px";
       return false;
    }
 
    phoneerr.innerHTML='<i class="fa-solid fa-circle-check" style="color:green"></i>';
     return true;
 }
 
 function addcheck(){
    let addip= document.getElementById("add").value;
    let adderr= document.getElementById("adderr");
    if(addip.length==0){
       adderr.innerHTML='<i class="fa-solid fa-circle-info"></i> Required';
       adderr.style.cssText="color:rgb(208, 43, 43); font-size:15px";
       return false;
    }
    if(addip.length<4){
       adderr.innerHTML='<i class="fa-solid fa-circle-info"></i> Invalid';
       adderr.style.cssText="color:rgb(208, 43, 43); font-size:15px";
       return false;
    }
 
    adderr.innerHTML='<i class="fa-solid fa-circle-check" style="color:green"></i>';
     return true;
 
 }
 
 homevisit.addEventListener("change",()=>{
    if(homevisit.checked){
       Array.from(type.options).forEach(option =>{
          if (option.value == "Standard Boarding" || option.value == "Premium Boarding") {
             option.style.display = "none"; 
         } else {
             option.style.display = "block"; 
         }
       });
    }
 })
 
 boarding.addEventListener("change", () => {
    if (boarding.checked) {  
        Array.from(type.options).forEach(option => {
            if (option.value == "Basic Home Visit" || option.value == "Extended Home Visit" || option.value == "Overnight Stay") {
                option.style.display = "none"; 
            } else {
                option.style.display = "block";
            }
        });
    }
 });
 
 type.addEventListener("change",()=>{
    if(type.value=="Basic Home Visit"){
      price.value=700;
    }else if(type.value=="Extended Home Visit"){
      price.value=1400;
    }else if(type.value=="Overnight Stay"){
      price.value=13400;
    }else if(type.value=="Standard Boarding"){
      price.value=75500;
    }else if(type.value=="Premium Boarding"){
      price.value=80500;
    }
 })


 function handleBookingSuccess(bookingId) {
   const downloadUrl = '/downloadPdf?bookingId=' + bookingId; 
   window.location.href = downloadUrl; 
  
   setTimeout(() => {
       window.location.href = '/home'; // Adjust the path as needed
   }, 3000); 
}


 
 
 