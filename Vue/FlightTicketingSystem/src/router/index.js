import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: () => import('@/layouts/mainlayouts.vue'),
      children:[
        {
          path:"airport",
          component:()=>import("@/page/admin/Airportspage.vue")
          
        },
        {
          path:"test",
          component:()=>import("@/page/admin/TEST.vue")
          
        },
        {
          path:"testtt",
          component:()=>import("@/page/admin/TESTTT.vue")
          
        },
        {
          path:"members",
          component:()=>import("@/page/admin/Memberpage.vue")
        },
        {
          path:"flight",
          component:()=>import("@/page/admin/Flight.vue")
        }

      ]
    }
   
    // {
    //   path: '/about',
    //   name: 'about',
    //   // route level code-splitting
    //   // this generates a separate chunk (About.[hash].js) for this route
    //   // which is lazy-loaded when the route is visited.
    //   component: () => import('../views/AboutView.vue'),
    // },
  ],
})

export default router;
