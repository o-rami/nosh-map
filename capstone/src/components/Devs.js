import githubIcon from "../images/github-icon-trs.png";
import linkedinIcon from "../images/linkedin-icon-trs.png";

import emailIcon from "../images/email-icon-trs.png";
import Header from "./Header";
import Dashboard from "./Dashboard";


// Major shrinkage issues, need to make dimensions fixed

function Devs() {

  return (<>
    <div className="flex mt-36">
      <div className="mt-1">
        <h1 className="text-left text-3xl uppercase font-bold ml-20">Meet the Devs</h1>
        <div className="listingSection justify-start flex">
          <div className="devContainer justify-center grid grid-cols-4 gap-4 ml-20 ">

          <div class="max-w-lg mx-auto h-[650px] max-h-full shrink-0 my-10 bg-[white] rounded-lg shadow-xl p-5 mb-96">
                <img class="w-64 h-64 rounded-full mx-auto  outline-2 outline-[white]"
                  src="https://media.licdn.com/dms/image/D5603AQEnhkxT4pTgKw/profile-displayphoto-shrink_800_800/0/1680828050779?e=1694044800&v=beta&t=OHG8Stjwm8LSxlhszJZY-O2uW1B9KqCU-BwCIZopJS0"
                  alt="Jordan's profile" />
                <h2 class="text-center text-[black] text-2xl font-semibold mt-3">Jordan Davis</h2>
                <p class="text-center text-[#575757] mt-1">Dev10 Associate</p>
                <div class="flex justify-center mt-2">
                  <a href="https://www.linkedin.com/in/jordan-davis-648949185/">
                    <img src={linkedinIcon} className="dev-icon h-10 w-auto" alt="linkedin" />
                  </a>
                  <a href="https://github.com/jojo130151">
                    <img src={githubIcon} className="dev-icon h-10 w-auto" alt="github" />
                  </a>
                </div>
                <div class="mt-2">
                  <p class="text-center text-[#575757]">While working in retail,
                    she got her Bachelor's in Computer Science and worked on growing her soft skills.
                    She is now a proud Dev10 asscoiate working to break into tech and challenge herself with a new career.</p>
                </div>
              </div>

              <div class="max-w-lg h-[650px] max-h-full shrink-0 my-10 bg-[white] rounded-lg shadow-xl p-5 mb-96">
                <img class="w-64 h-64 rounded-full mx-auto outline-2 outline-[white]"
                  src="https://media.licdn.com/dms/image/D5603AQE3_UU_2YV8JA/profile-displayphoto-shrink_200_200/0/1679943257838?e=1694044800&v=beta&t=zqr1Lq3_GqQGThQmzCCEvyMzsqsJYSmW1vQrvprES48"
                  alt="Nou's profile" />
                <h2 class="text-center text-[black] text-2xl font-semibold mt-3">Nou Vang</h2>
                <p class="text-center text-[#575757] mt-1">Dev10 Associate</p>
                <div class="flex justify-center mt-2">
                  <a href="https://www.linkedin.com/in/nou-v-8a766166/">
                    <img src={linkedinIcon} className="dev-icon h-10 w-auto" alt="linkedin" /></a>
                  <a href="https://github.com/nomvaa">
                    <img src={githubIcon} className="dev-icon h-10 w-auto" alt="github" /></a>
                </div>
                <div class="mt-2">
                  <p class="text-center text-[#575757]">Nou graduated from thhe University of Minnesota with a B.S in Psychology and minor in Early Childhood Education.
                  Having previously worked in Property Management for commerical restate, Nou is excited about transitioning into a career as a software developer. 
                 Today, she is estatic to be a part of the Dev10 organization.
                  </p>
                </div>
              </div>

              <div class="max-w-lg h-[650px] max-h-full shrink-0 my-10 bg-[white] rounded-lg shadow-xl p-5 mb-96">
                <img class="w-64 h-64 rounded-full mx-auto"
                  src="https://i.postimg.cc/g01yYTBh/me.jpg"
                  alt="Oscar's profile" />
                <h2 class="text-center text-[black] text-2xl font-semibold mt-3">Oscar Ramirez</h2>
                <p class="text-center text-[#575757] mt-1">Dev10 Associate</p>
                <div class="flex justify-center mt-2">
                  <a href="https://www.linkedin.com/in/oscar-ramirez-91301a267/">
                    <img src={linkedinIcon} className="dev-icon h-10 w-auto" alt="linkedin" /></a>
                  <a href="https://github.com/o-Rami">
                    <img src={githubIcon} className="dev-icon h-10 w-auto" alt="github" />
                  </a>
                </div>
                <div class="mt-2">
                  <p class="text-center text-[#575757]">Oscar graduated from City College CUNY with a BA in Psychology.
                    He spent several years working as a NASM CPT, an F02 Security/Fire Guard, and grocery team member before attending NYU's Tandon Bridge Program,
                    where he discovered a love for problem solving and software development.
                    Today, he's honored to be a Dev10 associate.</p>
                </div>
              </div>
              </div>
              </div>

            </div>

          </div>
        </>
        );
}

        export default Devs;


// [#e56c21] Nosh Icon
// [#cfc6c6] Background
// [#d15123] Footer
// [#904415] orange/brown alt 1
// [#3b1c09] orange/brown alt 2