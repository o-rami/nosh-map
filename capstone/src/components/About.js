import appOnPhone from "../images/About1.png";
import knivesIcon from "../images/knives.png";

function About() {

  // TO DO:
  // - looking for stock photos/videos 

  return <>
  
  <div className="mt-32"></div>
    <div className="flex bg-[white]">

      <div className="min-w-md flex  w-[100vw] h-[50-vh] px-48 bg-[white] py-4">
        <div className="mt-20 h-[50-vh] bg-[white] p-8">
          <h1 className="text-6xl text-[#e56c21] uppercase font-bold leading-4 px-2">Nosh<span className="text-[black]">.Map</span></h1>
          <p className="text-2xl font-medium text-[black] leading-2 mt-12 px-2">Welcome to Nosh.Map, your go-to app to explore and document the best culinary experiences in your city. Whether you're a foodie, a connoisseur seeking new flavors, or simply someone looking for a great place to eat, Nosh.Map has got you covered!</p>

          <p className="text-xl font-medium text-[black] leading-2 my-12 px-2">At Nosh.Map, we're passionate about connecting people with their perfect dining experiences.
            We make it easy for you to find what you're craving, no matter your preferences.</p>

          <div className="flex justify-left my-4 py-6 px-2">
            <img src={knivesIcon} className="h-12 w-auto mr-4" />
            <p className="text-[black] text-lg">Explore, rate and review nearby hotspots, and even create a curated list of personal favorites.</p>
          </div>

          <div className="flex justify-left my-4 py-6 px-2">
            <img src={knivesIcon} className="h-12 w-auto mr-4" />
            <p className="text-[black] text-lg">Nosh.Map is more than just a restaurant directory. It's a community of food enthusiasts sharing their experiences and recommendations.</p>
          </div>

          <div className="flex justify-left my-4 py-6 px-2">
            <img src={knivesIcon} className="h-12 w-auto mr-4" />
            <p className="text-[black] text-lg">Plan your foodie adventure!</p>
          </div>

          <div className="flex justify-left mt-4 py-6 px-2">
            <img src={knivesIcon} className="h-12 w-auto mr-4" />
            <p className="text-[black] text-lg">Tantalize your taste buds and satisfy your cravings with Nosh.Map!</p>
          </div>

        </div>

        <img src={appOnPhone} className="h-[50-vh] min-w-lg rounded bg-[white]" />
      </div>
    </div>
  </>
}


export default About;