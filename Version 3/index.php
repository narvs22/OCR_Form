<!DOCTYPE html>
<html lang="en">


<head>
	<!-- Setting the pages character encoding -->
	<meta charset="UTF-8">

	<!-- The meta viewport will scale my content to any device width -->
	<meta name="viewport" content="width=device-width, initial-scale=1.0">

	<!-- Link to my stylesheet -->
	<link rel="stylesheet" href="styles.css">
	<title>Finished</title>
</head>
<body>




<!-- Title of Table -->
	<h2>Applicants Data 2022</h2>



	<table id="myTable">
		<!-- Column Headers -->
		<tr>

		    <th onclick="sortTable(0)">UserID</th>
			<th onclick="sortTable(1)">First Name</th>
            <th  onclick="sortTable(2)">Surname</th>
            <th  onclick="sortTable(3)">DOB</th>
            <th  onclick="sortTable(4)">Address</th>
            <th  onclick="sortTable(5)">Contact</th>
            <th  onclick="sortTable(6)">Cause of Condition</th>
            <th  onclick="sortTable(7)">Insurance Company</th>

		</tr>
	

		<!-- Filling up the rows with json information -->

		<?php

			$json_data = file_get_contents("FormSubmission.json");

			$forms = json_decode($json_data, true);
			
			if(count($forms) != 0)
			{

				$a=0;
				
				foreach ($forms as $form) 
				{
					?>
						<tr>

						   <td>

						   <?php

                           
						   
						   echo "$a";

						   $a=$a+1;
						   ?>
								

						  
								
							</td>
					

							<td>
								<?php echo $form['First Name'];?>

								<?php
								if ($form['First Name']=="")
								{
									echo "N/A";
								}
								?>
							</td>
								
							
							<td>

								<?php echo $form['Surname']; ?>

								<?php
								if ($form['Surname']=="")
								{
									echo "N/A";
								}
								?>

							</td>


							<td>
								<?php echo $form['DOB']; ?>

								<?php
								if ($form['DOB']=="")
								{
									echo "N/A";
								}
								?>

							</td>


							<td>
								<?php echo $form['Address']; ?>

								<?php
								if ($form['Address']=="")
								{
									echo "N/A";
								}
								?>

							</td>


							<td>
								<?php echo $form['Contact Number']; ?>

								<?php
								if ($form['Contact Number']=="")
								{
									echo "N/A";
								}
								?>

							</td>


							<td>
								<?php echo $form['Cause of Condition']; ?>

								<?php
								if ($form['Cause of Condition']=="")
								{
									echo "N/A";
								}
								?>

							</td>


							<td>
								<?php echo $form['Insurance Company']; ?>

								<?php
								if ($form['Insurance Company']=="")
								{
									echo "N/A";
								}
								?>

							</td>
						</tr>
					<?php
				}
			}
		?>
	</table>



<script>
function sortTable(n) {
  var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
  table = document.getElementById("myTable");
  switching = true;
  //Set the sorting direction to ascending:
  dir = "asc"; 
  /*Make a loop that will continue until
  no switching has been done:*/
  while (switching) {
    //start by saying: no switching is done:
    switching = false;
    rows = table.rows;
    /*Loop through all table rows (except the
    first, which contains table headers):*/
    for (i = 1; i < (rows.length - 1); i++) {
      //start by saying there should be no switching:
      shouldSwitch = false;
      /*Get the two elements you want to compare,
      one from current row and one from the next:*/
      x = rows[i].getElementsByTagName("TD")[n];
      y = rows[i + 1].getElementsByTagName("TD")[n];
      /*check if the two rows should switch place,
      based on the direction, asc or desc:*/
      if (dir == "asc") {
        if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
          //if so, mark as a switch and break the loop:
          shouldSwitch= true;
          break;
        }
      } else if (dir == "desc") {
        if (x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {
          //if so, mark as a switch and break the loop:
          shouldSwitch = true;
          break;
        }
      }
    }
    if (shouldSwitch) {
      /*If a switch has been marked, make the switch
      and mark that a switch has been done:*/
      rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
      switching = true;
      //Each time a switch is done, increase this count by 1:
      switchcount ++;      
    } else {
      /*If no switching has been done AND the direction is "asc",
      set the direction to "desc" and run the while loop again.*/
      if (switchcount == 0 && dir == "asc") {
        dir = "desc";
        switching = true;
      }
    }
  }
}
</script>

</body>
</html>
