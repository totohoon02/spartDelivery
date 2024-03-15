const dashboard = document.querySelector("#dashboard")
const business = document.querySelector("#business")
const menu = document.querySelector("#menu")
const order = document.querySelector("#order")

dashboard.addEventListener("click", ()=>{window.location.href="/dashboard"});
business.addEventListener("click", ()=>{window.location.href="/business"});
menu.addEventListener("click", ()=>{window.location.href="/menu"});
order.addEventListener("click", ()=>{window.location.href="/order"});
